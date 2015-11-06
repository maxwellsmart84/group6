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
    page.videBackground();

  },

  videBackground: function (){
    $('#frontPage').vide('DreamBig.mp4');
    $('#myBlock').vide('DreamBig.mp4', {
        volume: 0,
        playbackRate: 1,
        muted: true,
        loop: true,
        autoplay: true,
        position: '50% 50%', // Similar to the CSS `background-position` property.
        posterType: 'png', // Poster image type. "detect" — auto-detection; "none" — no poster; "jpg", "png", "gif",... - extensions.
        resizing: true, // Auto-resizing, read: https://github.com/VodkaBears/Vide#resizing
        bgColor: 'transparent' // Allow custom background-color for Vide div
    });
  },


};
