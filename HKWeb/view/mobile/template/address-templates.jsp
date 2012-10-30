<script type="text/template" id="address-view-template2">
	<h2>Address 1</h2>
   	Plot No:233<br>
  	Sector 14, Gurgaon<br>
   	Pin-123231, Haryana
   	<div>
		<a href='#'>Delete</a>
	</div>
 </script>
 <script type='text/template' id='address-view-template'>
	{{for(var i =0;i<data.length;i++){ }}			
		<li  class='shadow-4-address arr-r'>
			<a class='addAddressToOrder' href='javascript:void(0)' addrId="{{print(data[i].id)}}">
				<table width='100%'>
					<tr>
					<td class='text-container'>
						<h3 style='white-space:normal;padding-left:10px;margin-top:0px'>{{print(data[i].name)}}</h3>
						<p style='margin:0px;padding-left:10px'>
							<span class='addressText'>{{print(data[i].line1)}}</span><br>
							<span class='addressText'>{{print(data[i].line2)}}</span><br>
							<span class='addressText'>{{print(data[i].city)}}</span>,
							<span class='addressText'>{{print(data[i].state)}}</span><br>
							<span class='addressText'>PIN Code:{{print(data[i].pin)}}</span><br>
							<span class='addressText'>Ph: {{print(data[i].phone)}}</span>
						</p>
					</td>
					</tr>
				</table>
			</a>
		</li>
	{{ } }}	
</script>