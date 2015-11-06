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
    page.grabActivityFromServer();

  },
  initEvents:function(){

    },
  getTemplate: function(name) {
    return _.template(templates[name]);
  },
  loadTemplate: function(name, val) {
      var tmpl = page.getTemplate(name);
      return tmpl(val);
  },
  getUser: function(){
      $.ajax({
        url: '/getUser',
        method: 'GET',
        success: function(data){
          userData = JSON.parse(data);
          page.loadTemplate($(''), userData, $('#userTmpl').html());

        }
      });
    },

  loadActivity : function(arr){
    _.each(arr, function(currVal, idx, arr){
    //maintains the same ID regardless of view
      if(arr === page.activityList){
      currVal.id = idx;
    }
    else{
      currVal.id = _.filter(page.activityList, function(obj){
        return obj.item === currVal.item;})[0].id;
    }

      if(currVal.status ==="Active"){
      page.loadTemplate($('.activity-section'), currVal, $('#activityTmpl').html());
    }
    else{
        toDo.loadTemplate($('.activity-section'), currVal, $('#completeActivityTmpl').html());

        }
      });
    },

  grabActivityFromServer: function() {
    $.ajax({
      type: 'GET',
      url: '/getActivity',
      success: function(data) {
        console.log("SUCCESS: ", data);
        activityData = JSON.parse(data);
        page.loadActivity(data);
      },
      failure: function(data) {
        console.log("FAILURE: ", data);
      }
    });
  },
  sendActivityToServer: function(activity) {
    console.log("IN TRANSIT", activity);
    $.ajax({
      url: '/getActivity',
      method: 'POST',
      data: activityData,
      success: function(resp) {
        var htmlForArticle = page.loadTemplate('#activityTmpl',resp);
        $('.activity').prepend(htmlForArticle);
        $('form > input[type="text"]').val('');
      },
      failure: function(resp) {
        console.log("FAILURE", resp);
      }
    });
  },


};
