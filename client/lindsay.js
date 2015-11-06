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
    page.grabBucketFromServer();

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

  grabBucketsFromServer: function() {
    $.ajax({
      type: 'GET',
      url: '/userBuckets',
      success: function(data) {
        console.log("SUCCESS: ", data);
        bucketData = JSON.parse(data);
        page.loadBucket(data);
      },
      failure: function(data) {
        console.log("FAILURE: ", data);
      }
    });
  },
  sendBucketsToServer: function(bucket) {
    console.log("IN TRANSIT", bucket);
    $.ajax({
      url: '/userBuckets',
      method: 'POST',
      data: bucketData,
      success: function(resp) {
        var htmlForArticle = page.loadTemplate('#bucketTmpl',resp);
        $('h4').prepend(htmlForArticle);

      },
      failure: function(resp) {
        console.log("FAILURE", resp);
      }
    });
  },


};
