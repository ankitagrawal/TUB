package assets.skins.button
{
	import flash.display.DisplayObject; 
	import flash.text.TextLineMetrics;
	
	import mx.controls.Button;
	import mx.core.UITextField; 
	
	public class MultiLineButton extends Button
	{
		public function MultiLineButton()
		{
			super();  
		} 
		override protected function createChildren():void
		{
			if (!textField)
			{
			textField  = new UITextField();
			textField.styleName = this;
			addChild(DisplayObject(textField));
			} 
			super.createChildren(); 
			textField.multiline = true;
			textField.wordWrap = true;
		}  		
		override public function measureText(s:String):TextLineMetrics
		{
			textField.text = s;
			var lineMetrics:TextLineMetrics = textField.getLineMetrics(0);
			textField.rotation = this.getStyle("btnRotation");
			if(textField.rotation !=0){
				lineMetrics.width =15 
				lineMetrics.height =27	
			}
			
			return lineMetrics;
		}
	}
}
	 