    <one-to-one
	    name="${property.name}"
	    class="${c2j.getJavaTypeName(property, false)}"
<#if !property.basicPropertyAccessor>
        access="${property.propertyAccessorName}"
</#if>
<#if property.cascade != "none">
        cascade="${property.cascade}"
</#if>
<#if property.value.constrained>
        constrained="true"
</#if>

<#if property.value.hasFormula()>
<#assign formula = c2h.getFormulaForProperty(property)>
<#if formula>
        formula="${formula.text}"
</#if>
</#if>
    >
   </one-to-one>
