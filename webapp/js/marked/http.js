/*
 * Symphony - A modern community (forum/SNS/blog) platform written in Java.
 * Copyright (C) 2012-2017,  b3log.org & hacpai.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * @fileoverview marked HTTP server.
 *
 * @author <a href="http://vanessa.b3log.org">Liyuan Li</a>
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.1.0.0, May 20, 2017
 * @since 1.7.0
 */

var PORT = 8250;

var http = require('http');
var marked = require('marked');
var renderer = new marked.Renderer();

renderer.listitem = function (text, level) {
    if (text.indexOf('[ ] ') === 0 && text.replace(/\s/g, '') !== '[]') {
        text = '<input type="checkbox" disabled>' + text.replace('[ ]', '');
        return `<li class="task-item">${text}</li>`;
    } else if (text.indexOf('[x] ') === 0 && text.replace(/\s/g, '') !== '[x]') {
        text = '<input type="checkbox" checked disabled>' + text.replace('[x]', '');
        return `<li class="task-item">${text}</li>`;
    }

    return `<li>${text}</li>`;
};

marked.setOptions({
    renderer: renderer,
    gfm: true,
    tables: true,
    breaks: true,
    smartLists: true
});

process.on('uncaughtException', function (err) {
    console.log(err);
});

var server = http.createServer(function (request, response) {
    var mdContent = '';

    request.on('data', function (data) {
        mdContent += data;
    });

    request.on('end', function () {
        response.write(marked(mdContent));

        response.end();
    });
});

server.listen(PORT);
console.log("Marked engine is running at port: " + PORT);
