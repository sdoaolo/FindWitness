const express = require('express')
const app = express();
const http = require('http');

const server = http.Server(app);

const socketio = require('socket.io')
const io = socketio(server);

var PORT = process.env.PORT || 8004;
var cluster = require('cluster');
cluster.schedulingPolicy = cluster.SCHED_RR;

server.listen(PORT, function () {
    console.log('Listening to PORTv ' + PORT);
});

const mysql = require('mysql2');
const connection = mysql.createConnection({
host : 'localhost',
port: 5385,
user: 'root',
password : 'witness5385',
database : 'CHAT_MESSAGE'
})

connect_chat_db();


var chatlist_sender = new Array();
var chatlist_sender_count =0;
var chatlist_last_msg = new Array();
var chatlist_last_time=new Array();
var chatlist_msg_count=new Array();

var moment = require('moment');


connect_chat_db();


var flag = 0;
var numUsers = 0;

console.log('numUsers'+numUsers);


io.on('connection', function (socket) {
    console.log('a new socket has connected');
    console.log('my socke.id? : '+socket.id);
    var addedUser = false;
    socket.on('chatlist add user', function (username) {

      if (addedUser == true) {
        console.log('user already added');
        return;
      }

      socket.username = username;
      numUsers++;
      console.log(username + " has chat list joined")

      chatlist_name(username);

  });


  socket.on('person', function () {
    setTimeout(function()
    {
      for (var i = chatlist_sender_count-1; i >=0; i--)
      {
        io.to(socket.id).emit('person',{
          user: chatlist_sender[i],
          text: chatlist_last_msg[i],
          time: chatlist_last_time[i],
          chat_num: chatlist_msg_count[i]
        });

      flag=0;
      }
    }, 100);

});


    socket.on('disconnect', function () {

      chatlist_sender_count =0;
      chatlist_sender = {};
      chatlist_last_msg ={};
      chatlist_last_time={};
      chatlist_msg_count={};

      if (addedUser == true) {
          numUsers--;
      }
    });
});


function connect_chat_db(){
  console.log("연결성공!");
}


function chatlist_name(receiver_sql){
  var sql = 'select distinct sender from chat where receiver=?';
  var param = [receiver_sql];

  connection.query(sql, param, function(err, rows, fields){
    if (err){
        console.log(err);
    }
    else{
      if(rows.length!=0)
      {

        for(var i = 0; i < rows.length; i++)
        {
          chatlist_sender[i]=rows[i].sender;
        }
        flag = 1;
        chatlist_sender_count=rows.length;

        if(flag==1)
        {
          for (var i = 0; i < chatlist_sender_count; i++)
          {
            chatlist_msg(receiver_sql,chatlist_sender[i],i);
          }
        }
      }
    }
});
}


function chatlist_msg(receiver_sql,sender_sql,i){
  var sql = 'select msg,time from chat where receiver=? and sender=?';
  var param = [receiver_sql,sender_sql];
  var index =i;

  connection.query(sql, param, function(err, rows, fields){
    if (err){
        console.log(err);
    }
    else{
      if(rows.length!=0)
      {

        chatlist_last_msg[index] = rows[rows.length-1].msg;
        chatlist_last_time[index] = rows[rows.length-1].time;
        chatlist_msg_count[index] = rows.length;
      }
    }
});
}
