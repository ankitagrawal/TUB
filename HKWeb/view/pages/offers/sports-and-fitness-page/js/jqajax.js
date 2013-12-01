function ajax(file_url,target,form,callback,method,async,refres)
{
	var asyn=true,fd="{}";
	var methode_type="GET";
	
	if(method)
		methode_type=method;
	
	if(async===false)
		asyn=false;
	
	if(form)
	{
		fd=$('#'+form).serialize();
	}
	
	
		$.ajax({
		  type: methode_type,
		  url: file_url,
		  data: fd,
		  async: asyn,
		  cache: false
		}).done(function( msg ) {
			if(form)
			{
				if(refres)
				{
					$('#'+form).each(function(){this.reset()});
				}
			}
			if(target)
			{
		  		$('#'+target).html(msg);
			}
		  if(callback)
		  {
			  eval(callback);
		  }
		});
}
