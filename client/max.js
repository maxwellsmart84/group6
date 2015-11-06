
videBackground: function (){
  $('body').vide('DreamBig.mp4');
  $('body').vide('DreamBig.mp4', {
      volume: 0,
      playbackRate:-3,
      muted: true,
      loop: true,
      autoplay: true,
      position: '100% 100%', // Similar to the CSS `background-position` property.
      posterType: 'png', // Poster image type. "detect" — auto-detection; "none" — no poster; "jpg", "png", "gif",... - extensions.
      resizing: true, // Auto-resizing, read: https://github.com/VodkaBears/Vide#resizing
      bgColor: 'transparent' // Allow custom background-color for Vide div
  });
},


newUserEvent: function(){
  $(".container").on("click", "#signUp", function(event){
    $("#inputEmail").removeClass("hidden-class");
    $("#inputAvatar").removeClass("hidden-class");
    $("#loginSubmit").removeClass("hidden-class");
    $("#signUp").addClass("hidden-class");
    $("#loginReturn").addClass("hidden-class");
  });
},


loginSub: function(){
 $(".container").on("click", "#loginSubmit", function(event){
   event.preventDefault;
   var $submitBtn = $(this);//WASNT SURE IF I NEEDED THIS
   $(".col-md-8").removeClass("hidden-class"); //REMOVES ALL HIDDEN CLASSES FROM CHATBOX
   $(".col-md-4").removeClass("hidden-class");
   $("#loginContainer").addClass("hidden-class");
   $(".navbar-default").removeClass("hidden-class");
   $(".messageWriter").removeClass("hidden-class");
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
