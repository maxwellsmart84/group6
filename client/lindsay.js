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
    createNewBucket: function() {
      var bucketText = $('form > input[name="bucket"]').val();
      var newBucket = {
        bucket: bucketText
      };
      // console.log("NEW bucket", newBucket)

      page.sendBucketsToServer(newBucket);
      // sendBucketsToServer

    },

    loadBucket: function(data) {
      var bucketHTML = "";
      _.each(data.reverse(), function (currVal, idx, arr) {
        if (currVal.userName === page.currentUser){
          var bucketTemplateCurrUser = _.template($('#bucketTmplCurrUser').html());
          bucketHTML += bucketTemplateCurrUser(currVal);
        } else {
          var bucketTemplate = _.template($('#bucketTmpl').html());
          bucketHTML += bucketTemplate(currVal);

      }
    });
  $('h4').html(bucketHTML);
  },

  grabBucketsFromServer: function() {
    $.ajax({
      type: 'GET',
      url: '/userBucket',
      success: function(data) {
        console.log("SUCCESSSSSSSSSSSSSSSSSSS: ", data);
        bucketData = JSON.parse(data);
        // page.loadBucket(data);
      },
      failure: function(data) {
        // console.log("FAILURE: ", data);
      }
    });
  },
  sendBucketsToServer: function(bucket) {
    // console.log("IN TRANSIT", bucket);
    $.ajax({
      url: '/insertBucket',
      method: 'POST',
      data: bucketData,
      success: function(resp) {
        var htmlForArticle = page.loadTemplate('#bucketTmpl',resp);
        $('h4').prepend(htmlForArticle);

      },
      failure: function(resp) {
        // console.log("FAILURE", resp);
      }
    });
  },
  setUser: function(name){
    page.currentUser = name;
  },


};
