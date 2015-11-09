
videBackground: function (){
  $('body').vide('DreamBig.mp4');
  $('body').vide('DreamBig.mp4', {
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
  $("#loginBox").on("click", "#loginClick", function(event){
    event.preventDefault();
    $("#loginBox").removeClass("hidden-class");
    $("#inputFirstName").addClass("hidden-class");
    $("#inputLastName").addClass("hidden-class");
    $("#loginSubmit").addClass("hidden-class");
  });
},


loginSub: function(){
 $("body").on("click", "#signUp", function(event){
   event.preventDefault;
   var $submitBtn = $(this);//WASNT SURE IF I NEEDED THIS
   $(".col-md-12").removeClass("hidden-class");
   var userName = $("input[name='username']").val();//USER INPUT COLLECTION STRINGIFIED
   var userEmail = $("input[name='email']").val();
   var userAvatar = $("input[name='avatar']").val();
   userLoginAdd = new User({  //ADDING DATA TO CONSTRUCTOR
     username: userName,
     email: userEmail,
     avatar: userAvatar,
   });
     $.ajax({     //AJAX PUSH TO SERVER
       url: page.urlU,
       method:"POST",
       data: userLoginAdd,
       success: function (data){
         console.log("SUCCESS!", data);
       },
       failure: function (data) {
         console.log("FAILURE!!!");
       }
     });
 });
},
