<html>
<head><title>Raport Catalog</title></head>
<body>
<h1>Catalog: ${name}</h1>
<ul>
    <#list items as item>
        <li><b>${item.title}</b> (Locatie: <a href="${item.location}">${item.location}</a>)</li>
    </#list>
</ul>
</body>
</html>