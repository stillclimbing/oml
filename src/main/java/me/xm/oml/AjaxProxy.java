package me.xm.oml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jwbroek.cuelib.CueParser;
import jwbroek.cuelib.CueSheet;
import jwbroek.cuelib.TrackData;
import me.xm.util.FileUtils;
import me.xm.util.ImageUtils;

public class AjaxProxy {

	static final String IMG_FORMAT = "jpg";
	static final String THUMB_IMG_FILE_NAME = "_thumb." + IMG_FORMAT;
	static final String COVER_IMG_FILE_NAME = "_cover." + IMG_FORMAT;

	public static void refresh() throws Exception {
		Collection<String> paths = SystemSetting.getInstance().getPaths()
				.values();
		for (String path : paths) {
			System.out.println("Scanning path:" + path);
			List<Album> as = scanPath(path);
			if (as == null)
				continue;
			System.out.println("Persisting albums:" + as.size());
			AlbumDAO.deleteByPathHash(path.hashCode());
			for (Album a : as) {
				AlbumDAO.insertAlbum(a);
			}
		}
	}

	public static List<Album> getAlbums() throws Exception {
		return AlbumDAO.getAll();
	}

	public static SystemSetting getSystemSetting() {
		return SystemSetting.getInstance();
	}

	/**
	 * 
	 * @param path
	 *            album path, one path contain one album.
	 * 
	 *            Path name must follow pattern, cue files in path are
	 *            considered as discs of the album. Track info in cue files will
	 *            be extracted. However, album name/performer/etc will be parsed
	 *            by folder name not from cue. jpg files consiedered as album
	 *            art tag file considered as tag
	 * 
	 * @return
	 * @throws IOException
	 */
	private static List<Album> scanPath(String path) throws IOException {
		File fname = new File(path);
		if (!fname.isDirectory()) {
			System.out.println("path not found. scan cancelled.");
			return null;
		}

		String outPutPath = SystemSetting.getInstance().getDataFolder()
				+ path.hashCode();
		System.out.println("generating data to:" + outPutPath);

		// clean target path
		FileUtils.deleteDirectory(new File(outPutPath));

		List<Album> albums = new ArrayList<Album>();

		File[] files = fname.listFiles();
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File o1, File o2) {
				return new Long(o1.lastModified()).compareTo(new Long(o2
						.lastModified()));
			}
		});
		for (File albumFolder : files) {
			if (albumFolder.isDirectory()) {
				Album albm = new Album();
				albm.setPathHash(path.hashCode());
				albums.add(albm);
				String albumFolderName = albumFolder.getName();
				albm.setFolderName(albumFolderName);
				albm.setLastModified(albumFolder.lastModified());

				// create target folder
				String targetfolder = outPutPath + "/" + albumFolderName;
				File f = new File(targetfolder);
				if (!f.exists())
					f.mkdirs();

				// process album name
				String albumName = albumFolderName.substring(0,
						albumFolderName.indexOf("["));
				albm.name = albumName;

				// process [artist][label][discId]
				Pattern p = Pattern.compile("\\[(.+?)\\]");
				Matcher m = p.matcher(albumFolderName);
				List<String> result = new ArrayList<String>();
				while (m.find())
					result.add(m.group(1));
				if (result.size() > 0) {
					for (int i = 0; i < result.size(); i++) {
						if (i == 0)
							albm.artist = result.get(i);
						else if (i == 1)
							albm.label = result.get(i);
						else if (i == 2)
							albm.discId = result.get(i);
					}
				}

				// process image file, tag file
				boolean imageFound = false;
				for (File file : albumFolder.listFiles()) {
					String fName = file.getName();
					if (fName.indexOf(THUMB_IMG_FILE_NAME) == 0) {
						FileUtils.copyFile(file, targetfolder + "/"
								+ THUMB_IMG_FILE_NAME);
						imageFound = true;
					} else if (fName.indexOf(COVER_IMG_FILE_NAME) == 0) {
						FileUtils.copyFile(file, targetfolder + "/"
								+ COVER_IMG_FILE_NAME);
						imageFound = true;
					} else if (!imageFound
							&& (fName.toUpperCase().indexOf(".JPG") > 0
									|| fName.toUpperCase().indexOf(".PNG") > 0 || fName
									.toUpperCase().indexOf(".GIF") > 0)) {
						ImageUtils.resizeImageWithHint(file.getPath(),
								targetfolder + "/" + THUMB_IMG_FILE_NAME,
								IMG_FORMAT);
						FileUtils.copyFile(file, targetfolder + "/"
								+ COVER_IMG_FILE_NAME);
						imageFound = true;
					} else if (fName.indexOf(".tag") > 0) {// process tag
															// files in
															// folder
						String tag = fName.substring(0,
								fName.lastIndexOf(".tag"));
						albm.addTag(tag);
					} else if (fName.indexOf(".txt") > 0) {
						// var fPath =
						// destFolder+"\\"+rootFolder.Name+"\\"+subFolder.Name+"\\_intro"+fName.substring(fName.lastIndexOf("."));
						// fso.copyFile(file.Path,fPath,1);
					}
				}
				if (imageFound) {
					albm.coverThumbFile = THUMB_IMG_FILE_NAME;
					albm.coverFile = COVER_IMG_FILE_NAME;
				}

				// process cue files
				List<File> cs = findCuesInPath(albumFolder.getPath());
				List<CueSheet> cues = new ArrayList<CueSheet>();
				for (File cue : cs) {
					CueSheet c = CueParser.parse(cue);
//					System.out.println(c.getTitle());
					// c.setComment("{penguin:true,audiophi 100:true, disc:5}");
					// c.setSongwriter("xmxm");
					// PrintWriter out = new
					// PrintWriter(cue.getParent()+"\\new.cue");
					// out.print(ser.serializeCueSheet(c));
					// out.close();
//					List<TrackData> ts = c.getAllTrackData();
//					for (TrackData td : ts)
//						System.out.println("----"+td.getNumber()+td.getTitle()+"-"+td.getPerformer());
//					System.out.println("- " + c.getTitle() + ", "
//							+ c.getPerformer() + ", " + c.getSongwriter());
					cues.add(c);
				}
				albm.setTotalDiscs(cues.size());
				String desc = "";
				for(CueSheet c:cues){
					if(cues.size()==1) desc+=c.getTitle()+"\n";
					List<TrackData> ts = c.getAllTrackData();
					for (TrackData td : ts)
						desc+=td.getNumber()+"."+td.getTitle()+"\n";
					desc+="\n";
				}
				albm.setDesc(desc);
				System.out.println("album scanned:" + albm.folderName + ","
						+ albm.lastModified+","+albm.desc+","+albm.totalDiscs);
			}
		}
		return albums;
	}

	private static List<File> findCuesInPath(String path) throws IOException {
		File fname = new File(path);
		if (!fname.isDirectory()) {
			System.out.println("path not found. scan cancelled.");
			return null;
		}

		List<File> cues = new ArrayList<File>();

		File[] files = fname.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				cues.addAll(findCuesInPath(file.getPath()));
			} else if (file.getName().toUpperCase().indexOf(".CUE") > 0) {
				cues.add(file);
			}
		}
		// System.out.println("folder scanned:" + path+ ", found "+cues.size());

		return cues;
	}

	public static void main(String[] atrgs) throws Exception {
		List<Album> files = AjaxProxy.scanPath("Z:\\Classical Music HD\\Classical");
		for (Album file : files) {
			System.out.println(file.toString());

		}
	}
}
