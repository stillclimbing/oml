/**
 * Data Proxy aims centralize managing data objecting actions
 */
DataProxy = {
	// Error code consistent with AjaxProxy.class
	AJAX_CODE_NAME_DUPLICATE: -101,

	//jsonrpc
	jsonrpc:		null
};

/**
 * init data proxy
 */
DataProxy.init = function(){
	document.write('<script type="text/javascript" src="js/AjaxProxy.js"></script>');
};
