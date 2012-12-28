package assets.skins.renderer
{
	import flash.events.Event;
	import mx.core.UIComponent;
	import spark.components.supportClasses.InteractionState;
	import spark.components.supportClasses.InteractionStateDetector;
	import spark.skins.spark.DefaultItemRenderer;
	
	[Style(name="textRollOverColor", type="uint", format="Color", inherit="yes", theme="spark")]
	[Style(name="textSelectionColor", type="uint", format="Color", inherit="yes", theme="spark")]
	
	public class DropListIemRenderer extends DefaultItemRenderer
	{
		
		private var interactionStateDetector:InteractionStateDetector;
		public function DropListIemRenderer()
		{
			interactionStateDetector = new InteractionStateDetector(this);
			interactionStateDetector.addEventListener(Event.CHANGE, interactionStateDetector_changeHandler);
		}
		override protected function updateDisplayList (unscaledWidth:Number,
													   unscaledHeight:Number):void
		{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			var selectionTextColor:* = getStyle("textSelectionColor");
			var textRollOverColor:* = getStyle("textRollOverColor");
			var normalColor:* = getStyle("color");
			if(selected){
				labelDisplay.setStyle("color",selectionTextColor);	
			}else if(interactionStateDetector.state == InteractionState.OVER){
				labelDisplay.setStyle("color",textRollOverColor);
			}else{
				labelDisplay.setStyle("color",normalColor);
			}
		}
		private function interactionStateDetector_changeHandler(event:Event):void
		{
			invalidateDisplayList();
		}
	}
}