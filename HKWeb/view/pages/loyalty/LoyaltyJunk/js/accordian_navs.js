var ContentHeight = 110;
var TimeToSlide = 250.0;

var openAccordion = '';
var currentDiv = "";

function runAccordion(index,div)
{
  var raj = div;
  window.raj = div;
  if(index === 6){
    var element = $('#Accordion2Content').jScrollPane();
      element.data().jsp.destroy();
    if(raj.parentElement.style.overflow == "hidden"){
      raj.parentElement.style.overflowX = "hidden";      
      raj.parentElement.style.overflowY = "scroll";
      raj.parentElement.style.height = "110px";
    }
    else{
      raj.parentElement.style.overflow = "hidden";
      raj.parentElement.style.height = "auto";
    }
  }
  /*else{
    raj.parentElement.style.overflowY = "scroll";
    raj.parentElement.style.overflowX = "hidden";
    //raj.parentElement.style.height = "110px";
  }*/
  div.childNodes[1].style.color = "rgb(89, 142, 255)";
  if(currentDiv != div && currentDiv != ""){
    currentDiv.childNodes[1].style.color = "rgb(85, 85, 85)";
  }
  currentDiv = div;
  var nID = "Accordion" + index + "Content";
  if(openAccordion == nID)
  {
    nID = '';
  }
    set(new Date().getTime(),TimeToSlide,openAccordion, nID,raj);
  //setTimeout(function(){animate(new Date().getTime(),TimeToSlide,openAccordion, nID );}, 33);
  //animate(new Date().getTime() , TimeToSlide,openAccordion,nID);
  openAccordion = nID;
}

function set(lastTick, timeLeft, closingId, openingId, raj)
{
   setTimeout(function(){animate(lastTick,timeLeft,closingId, openingId,raj );}, 1);
}
function animate(lastTick, timeLeft, closingId, openingId,raj)
{
  var curTick = new Date().getTime();
  var elapsedTicks = curTick - lastTick;
  
  var opening = (openingId == '') ? null : document.getElementById(openingId);
  var closing = (closingId == '') ? null : document.getElementById(closingId);
 
  if(timeLeft <= elapsedTicks)
  {
    if(opening != null)
      opening.style.height = ContentHeight + 'px';
    
    if(closing != null && openingId === "Accordion6Content")
    {
      //console.log("Hello!");
    }
    else if(closing != null){
      closing.style.display = 'none';
      closing.style.height = '0px';
    }
    return;
  }
 
  timeLeft -= elapsedTicks;
  var newClosedHeight = Math.round((timeLeft/TimeToSlide) * ContentHeight);

  if(opening != null)
  {
    if(opening.style.display != 'block')
      opening.style.display = 'block';
    $('.AccordionContent').jScrollPane();
    if(openingId === "Accordion6Content"){
      opening.style.background = "rgb(221, 221, 221)";
      opening.style.width = "200px";
      var element = $('#Accordion2Content');
      element.jScrollPane().data().jsp.destroy();
    }
    else{
      opening.style.background = "rgb(241, 241, 241)";
    }
      opening.style.height = (ContentHeight - newClosedHeight) + 'px';
  }
  if(closing != null && openingId === "Accordion6Content"){
    $('#Accordion2Content').css("background","rgb(241, 241, 241)");
  }
  else if(closing != null)
  {
    $('#Accordion2Content').jScrollPane();
    closing.style.height = newClosedHeight + 'px';
  }
    set(curTick,timeLeft,closingId, openingId);
    //setTimeout(function(){animate(curTick,timeLeft,closingId,openingId);}, 33);
  //animate(curTick,timeLeft,closingId,openingId);
}

