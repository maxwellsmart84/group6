$(document).ready(function () {
  page.init();
});
var page= {
  init:function(){
    page.initStyling();
    page.initEvents();
  },
  initStyling:function(){
    page.getUser();

  },
  initEvents:function(){

    getUser: function(){
      $.ajax({
        url: '/getUser',
        method: 'GET',
        success: function(data){
          userData = JSON.parse(data);
          page.loadTmple($(''))

        }
      })
    }

  },

};
