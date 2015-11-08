$(document).ready(function () {
  page.init();
});
var page= {
  init:function(){
    page.initEvents();
    page.initStyling();


  },
  initStyling:function(){
    page.videBackground();

  },
  initEvents:function(){
    page.loginEvent();
    page.editItem();
    page.deleteBucket();
    page.createItemClick();
    page.videBackground();

  },



  videBackground: function (){
    // $('#frontPage').vide('DreamBig.mp4');
    $('#frontPage').vide('DreamBig.mp4', {
        volume: 0,
        playbackRate: .4,
        muted: true,
        loop: true,
        autoplay: true,
        position: '50% 50%', // Similar to the CSS `background-position` property.
        posterType: 'png', // Poster image type. "detect" — auto-detection; "none" — no poster; "jpg", "png", "gif",... - extensions.
        resizing: true, // Auto-resizing, read: https://github.com/VodkaBears/Vide#resizing
        bgColor: 'transparent' // Allow custom background-color for Vide div
    });
  },

  loginEvent: function(){
    $("#frontPage").on("click", "#loginClick", function(event){
      event.preventDefault();
      $("#loginBox").removeClass("hidden-class");
    });
  },

  newUserEvent: function(){
    $("#loginBox").on("click", "#signUp", function(event){
      event.preventDefault();
      console.log(event.target);
      $("#inputFirstName").removeClass("hidden-class");
    });
  },


  editItem: function() {
    $('section').on('click', '.listItem', function (event){
      event.preventDefault();
      $(this).closest('.listItem').replaceWith('<input type="text" class="updateListItem" placeholder="Edit Bucket List Item" name="updateListItem"</input>');
      $('.updateListItem').parent().siblings('.editItem').addClass('show');
    });

    $('.inputs').on('click', '#editedItem', function (event) {
      event.preventDefault();
      var itemId = $('.updateListItem').closest('article').data('itemid');
      var editedListItem = {
        title: $('.updateListItem').val(),
        complete: false
      }
      page.updateItem(itemId, editedListItem);

    });
  },

  deleteBucket: function(){
    $('section').on('click', '.deleteItem', function (event){
      event.preventDefault();
      var taskId = $(this).closest('article').data('itemid');
      page.deleteItem(taskId);
    });
  //strikethrough when click the check
    $('section').on('click', '.completeItem', function(event){
      event.preventDefault();
      $(this).parent().siblings('h4').toggleClass('complete')

    });
  },

  createItemClick: function (){
  $('.createItem').on('submit', function(event){
      event.preventDefault();
        var newItem = {
          title: $(this).find('input[name="newTitle"]').val(),
          complete: false,

        };
      page.createItem(newItem);
    });
  },


  createItem: function(newItem) {
    $.ajax({
      url: "/",
      data: newItem,
      type: 'POST',
      success: function (data) {
        console.log("SUCCESSFULLY CREATED NEW BUCKET", data);
        page.getItem();
      },
      failure: function (err) {
        console.log("DID NOT CREATE NEW BUCKET", err);
      }
    });
    $('input').val('');

    },

    deleteItem: function(itemId) {
      $.ajax({
        url: page.url + "/" + itemId,
        type: 'DELETE',
        success: function (data) {
          console.log("Delete success!", data);
          page.getItem();
        },
        failure: function (err) {
          console.log("delete failed",err);
        }
      });
    },


    updateItem: function(itemId, editedItem) {
      $.ajax({
        url: page.url + "/" + itemId,
        type: 'PUT',
        data: editedItem,
        success: function(data) {
          console.log("update success!", data);
          page.getItem();

        },

        failure: function(err) {
          console.log("update failure", err);
        }
      });
    },

};
