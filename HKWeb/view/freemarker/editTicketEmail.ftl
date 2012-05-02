Re: [ticket#${ticket.id}] Type - ${ticket.ticketType.name} - ${ticket.shortDescription}
Hi!

<div>
  Change Summary:
  <ul class="cont">
    <#if displayTicketHistoryDto.ownerChangeMessage??>
      <li><strong>Owner:</strong> ${displayTicketHistoryDto.ownerChangeMessage}</li>
    </#if>
    <#if displayTicketHistoryDto.statusChangeMessage??>
      <li><strong>Status:</strong> ${displayTicketHistoryDto.statusChangeMessage}</li>
    </#if>
    <#if displayTicketHistoryDto.typeChangeMessage??>
      <li><strong>Type:</strong> ${displayTicketHistoryDto.typeChangeMessage}</li>
    </#if>
    <#if displayTicketHistoryDto.shortDescriptionChangeMessage??>
      <li><strong>Short Description:</strong> ${displayTicketHistoryDto.shortDescriptionChangeMessage}</li>
    </#if>
    <#if displayTicketHistoryDto.fullDescriptionChangeMessage??>
      <li><strong>Full Description:</strong> ${displayTicketHistoryDto.fullDescriptionChangeMessage}</li>
    </#if>
    <#if displayTicketHistoryDto.associatedOrderChangeMessage??>
      <li><strong>Associated Order:</strong> ${displayTicketHistoryDto.associatedOrderChangeMessage}</li>
    </#if>
    <#if displayTicketHistoryDto.associatedUserChangeMessage??>
      <li><strong>Associated User:</strong> ${displayTicketHistoryDto.associatedUserChangeMessage}</li>
    </#if>
    <#if displayTicketHistoryDto.associatedEmailChangeMessage??>
      <li><strong>Associated Email:</strong> ${displayTicketHistoryDto.associatedEmailChangeMessage}</li>
    </#if>
    <#if displayTicketHistoryDto.associatedPhoneChangeMessage??>
      <li><strong>Associated Phone:</strong> ${displayTicketHistoryDto.associatedPhoneChangeMessage}</li>
    </#if>
    <#if displayTicketHistoryDto.associatedTrackingIdChangeMessage??>
      <li><strong>Associated Tracking Id:</strong> ${displayTicketHistoryDto.associatedTrackingIdChangeMessage}</li>
    </#if>
    <#if displayTicketHistoryDto.associatedCourierChangeMessage??>
      <li><strong>Associated Courier:</strong> ${displayTicketHistoryDto.associatedCourierChangeMessage}</li>
    </#if>
  </ul>
  <#if displayTicketHistoryDto.comment??>
    <span>${displayTicketHistoryDto.comment}</span><br/>
  </#if>
  <p class="sml gry" style="text-align: right;">${displayTicketHistoryDto.changedDate} changed
    by ${displayTicketHistoryDto.changedBy.name} (${displayTicketHistoryDto.changedBy.email})</p>
  <hr style="border: 1px dotted darkgray"/>
</div>
