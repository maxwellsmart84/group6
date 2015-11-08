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
    page.videBackground();
    page.newUserEvent();


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
      $("#signUpBox").addClass("hidden-class")
    });
  },

  newUserEvent: function(){
    $("#loginBox").on("click", "#signUp", function(event){
      event.preventDefault();
      console.log(event.target);
      $("#loginBox").addClass("hidden-class");
      $("#signUpBox").removeClass("hidden-class");
    });
  }
}
