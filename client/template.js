
  var templates = {

    bucketTmpl: ['<article class="bucket" data-index="<%= _id %>">',
               "<p><%= bucket %></p>",
              ' <button class="delete">Delete</button>',
              ' <button class="edit">Edit</button>',
              '<section class="edit-field">',
              '<input type="text" name="bucket" value="<%= bucket %>" />',
              '<input type="submit" name="edit" class="nothing" />',
              '</section></article>'
              ].join("")

  }
