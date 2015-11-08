var templates = {};

templates.bucket=[
'<article data-itemid="<%= _id %>">',
'<div class="bucketLineItem">',
'<span>',
'<a class="completeItem" href="#">',
'<i class="fa fa-check fa-2x">',
'</i>',
'</a>',
'</span>',
'<h4 class="listItem"><%= title %></h4>',
'<span>',
'<a class="deleteItem" href="#">',
'<i class="fa fa-trash-o fa-2x">',
'</i>',
'</a>',
'</span>',
'</div>',
'<div class="editItem">',
'<button id="editedItem" class="btn btn-primary">Edit Item</button>',
'</div>',
'</article>'

].join("");
