<html>
<head>
    <title>Action Logging Event Details</title>
    <style>

    /* FONT STACK */
    body, input, select, textarea {
        font-family: "HelveticaNeue-Light", "Helvetica Neue Light", "Helvetica Neue", Helvetica, Arial, "Lucida Grande", sans-serif;
    }

    h1, h2, h3, h4, h5, h6 {
        line-height: 1.1;
    }

    /* BASE LAYOUT */

    html {
        background-color: #ddd;
        background-image: -moz-linear-gradient(center top, #aaa, #ddd);
        background-image: -webkit-gradient(linear, left top, left bottom, color-stop(0, #aaa), color-stop(1, #ddd));
        background-image: linear-gradient(top, #aaa, #ddd);
        filter: progid:DXImageTransform.Microsoft.gradient(startColorStr = '#aaaaaa', EndColorStr = '#dddddd');
        background-repeat: no-repeat;
        height: 100%;
        /* change the box model to exclude the padding from the calculation of 100% height (IE8+) */
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
    }

    html.no-cssgradients {
        background-color: #aaa;
    }

    .ie6 html {
        height: 100%;
    }

    html * {
        margin: 0;
    }

    body {
        background: #ffffff;
        color: #333333;
        margin: 0 auto;
        width: 98%;
        -moz-box-shadow: 0 0 0.3em #255b17;
        -webkit-box-shadow: 0 0 0.3em #255b17;
        box-shadow: 0 0 0.3em #255b17;
    }


    /* replace with .no-boxshadow body if you have modernizr available */
    .ie6 body,
    .ie7 body,
    .ie8 body {
        border-color: #255b17;
        border-style: solid;
        border-width: 0 1px;
    }

    .ie6 body {
        height: 100%;
    }

    a:link, a:visited, a:hover {
        color: #48802c;
    }

    a:hover, a:active {
        outline: none; /* prevents outline in webkit on active links but retains it for tab focus */
    }

    h1 {
        color: #48802c;
        font-weight: normal;
        font-size: 1.25em;
        margin: 0.8em 0 0.3em 0;
    }


    .content h1 {
        border-bottom: 1px solid #CCCCCC;
        margin: 0.8em 1em 0.3em;
        padding: 0 0.25em;
        padding-top: 10px;
    }

    .scaffold-list h1 {
        border: none;
    }


    /* TABLES */

    table {
        border-top: 1px solid #DFDFDF;
        border-collapse: collapse;
        width: 100%;
        margin-bottom: 1em;
    }

    tr {
        border: 0;
    }

    tr>td:first-child, tr>th:first-child {
        padding-left: 1.25em;
    }

    tr>td:last-child, tr>th:last-child {
        padding-right: 1.25em;
    }

    td, th {
        line-height: 1.5em;
        padding: 0.5em 0.6em;
        text-align: left;
        vertical-align: top;
    }

    th, td.head {
        background-color: #efefef;
        background-image: -moz-linear-gradient(top, #ffffff, #eaeaea);
        background-image: -webkit-gradient(linear, left top, left bottom, color-stop(0, #ffffff), color-stop(1, #eaeaea));
        filter: progid:DXImageTransform.Microsoft.gradient(startColorStr = '#ffffff', EndColorStr = '#eaeaea');
        -ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorStr='#ffffff', EndColorStr='#eaeaea')";
        color: #666666;
        font-weight: bold;
        line-height: 1.7em;
        padding: 0.2em 0.6em;
    }

    thead th {
        white-space: nowrap;
    }

    .odd {
        background: #f7f7f7;
    }

    .even {
        background: #ffffff;
    }

    tr:hover {
        background: #E1F2B6;
    }

    /* PAGINATION */

    .pagination {
        border-top: 0;
        margin: 0;
        padding: 0.3em 0.2em;
        text-align: center;
        -moz-box-shadow: 0 0 3px 1px #AAAAAA;
        -webkit-box-shadow: 0 0 3px 1px #AAAAAA;
        box-shadow: 0 0 3px 1px #AAAAAA;
        background-color: #EFEFEF;
    }

    .pagination a {
        color: #666666;
        display: inline-block;
        margin: 0 0.1em;
        padding: 0.25em 0.7em;
        text-decoration: none;
        -moz-border-radius: 0.3em;
        -webkit-border-radius: 0.3em;
        border-radius: 0.3em;
    }

    .pagination a:hover, .pagination a:focus {
        background-color: #999999;
        color: #ffffff;
        outline: none;
        text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.8);
    }

    .no-borderradius .pagination a:hover, .no-borderradius .pagination a:focus {
        background-color: transparent;
        color: #444444;
        text-decoration: underline;
    }

    </style>
</head>
<body>
<div class="content scaffold-list">
    <h1>Action Logging Event Details</h1>
    <div class="pagination">
        <a href="${createLink(action: 'index', params: [offset: params.int('offset')])}">Back</a>
    </div>
    <table>
        <thead>
        <tr>
            <th>Controller Name</th>
            <th>Action Name</th>
            <th>Custom Action Name</th>
            <th>Action Type</th>
            <th>Status</th>
            <th>Exception Message</th>
            <th>Total Time (Secconds)</th>
            <th>Date &amp; Time</th>
            <th>Forward URI</th>
            <th>Remote Host</th>
            <th>Ajax Request</th>
            <th>User Id</th>
            <th>Params</th>
        </tr>
        </thead>
        <tbody>
        <g:if test="${event}">
            <tr class="even">
                <td>${event.controllerName}</td>
                <td>${event.actionName}</td>
                <td>${event.customActionName}</td>
                <td>${event.actionType}</td>
                <td>${event.status}</td>
                <td>${event.exceptionMessage}</td>
                <td>${event.time / 1000 / 60}</td>
                <td>${event.date}</td>
                <td>${event.forwardURI}</td>
                <td>${event.remoteHost}</td>
                <td>${event.ajax}</td>
                <td>${event.userId}</td>
                <td>${event.params}</td>
            </tr>
            <tr>
                <td class="head" colspan="13">Custom Log</td>
            </tr>
            <tr>
                <td colspan="13">
                    ${event.htmlCustomLog?.encodeAsRaw()}
                </td>
            </tr>
            <tr>
                <td class="head" colspan="13">Exception Stack Trace</td>
            </tr>
            <tr>
                <td colspan="13">
                    ${event.htmlExceptionStackTrace.encodeAsRaw()}
                </td>
            </tr>
        </g:if>
        <g:else>
            <tr>
                <td colspan="13"><center>No record found</center></td>
            </tr>
        </g:else>
        </tbody>
    </table>
</div>
</body>
</html>
