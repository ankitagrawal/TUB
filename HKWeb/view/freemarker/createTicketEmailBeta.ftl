[ticket#${ticket.id}] Type - ${ticket.ticketType.name} - ${ticket.shortDescription}
Hi!

<div style="background-color:beige; padding: 5px; margin-bottom: 10px; border: 1px solid darkgray; width: 600px;">
  <div style="position: relative; float: right;">
    <span class="sml gry">Created: ${ticket.createDate}</span>
    <#if ticket.createDate != ticket.updateDate>
      <br/><span class="sml gry">Updated: ${ticket.updateDate}</span>
    </#if>
  </div>
  <h3>${ticket.shortDescription}</h3>

  <p style="margin-bottom:1em">
   <#if ticket.fullDescription??>
     ${ticket.fullDescription}
   </#if>
  </p>
  <hr style="border: 1px dotted darkgray"/>
  <div style="position: relative; float: right;">
    <span class="sml gry">Reported By: ${ticket.reporter.name} (${ticket.reporter.email})</span><br/>
    <span class="sml gry">Assigned To: ${ticket.owner.name} (${ticket.owner.email})</span>
  </div>
  <div>
    <em>Ticket context data</em><br/>
    <#if ticket.associatedUser??>
      <span class="sml gry">Associated User:</span>
        ${ticket.associatedUser.email}
    </#if>
    <#if ticket.associatedOrder??>
      <span class="sml gry">Associated Order:</span>
        ${ticket.associatedOrder.id}
      <br/>
    </#if>
    <#if ticket.associatedEmail??>
      <span class="sml gry">Associated Email:</span> ${ticket.associatedEmail}<br/>
    </#if>
    <#if ticket.associatedPhone??>
      <span class="sml gry">Associated Phone:</span> ${ticket.associatedPhone}<br/>
    </#if>
    <#if ticket.associatedTrackingId??>
      <span class="sml gry">Associated Tracking Id:</span>
        ${ticket.associatedTrackingId}
      <br/>
    </#if>
    <#if ticket.associatedCourier??>
      <span class="sml gry">Associated Courier:</span> ${ticket.associatedCourier.name}<br/>
    </#if>
  </div>
</div>

