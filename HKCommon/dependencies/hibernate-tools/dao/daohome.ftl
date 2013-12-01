${pojo.getPackageDeclaration()}
// Generated ${date} by Hibernate Tools ${version}

<#assign classbody>
<#assign declarationName = pojo.importType(pojo.getDeclarationName())>/**
* DAO for domain model class ${declarationName}.
* @see ${pojo.getQualifiedDeclarationName()}
* @author Hibernate Tools
*/
<#if ejb3>
@${pojo.importType("javax.ejb.Stateless")}
</#if>
public class ${declarationName}Dao extends BaseDao<${declarationName}, ${pojo.getJavaTypeName(clazz.identifierProperty, jdk5)}>{

    public ${declarationName}Dao() {
        super(${declarationName}.class);
    }

}
</#assign>

${pojo.generateImports()}
${classbody}
