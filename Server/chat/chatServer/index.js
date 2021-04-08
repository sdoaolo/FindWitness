
const express = require('express')
const app = express();

const http = require('http');

const server = http.Server(app);

const socketio = require('socket.io');
const io = socketio(server);
var PORT = process.env.PORT || 8005;
server.listen(PORT, function () {
    console.log('Listening to PORTv ' + PORT);
});

const mysql = require('mysql2');
const connection = mysql.createConnection({
host : 'localhost',
port: 3306,
user: 'root',
password : '',
database : 'CHAT_MESSAGE'
})

connect_chat_db();

var input_num = 1;
var input_chat_room_name='';
var input_sender = '';
var input_receiver = '';
var input_msg = '';
var input_date = 0;
var input_time = 0;

var remaining_messages = new Array();
var remaining_sender = new Array();
var remaining_msg_true = 'no';

var moment = require('moment');


connect_chat_db();


var flag = 0;
var id=0;
var nicknames=[];
var ids=[];
var numUsers = 0;
var count = 0;
console.log('numUsers'+numUsers);


io.on('connection', function (socket) {

   console.log('a new socket has connected');
   var addedUser = false;

    socket.on('new message', function (data,rcv,sender,roomname) {
      id=0;
      socket.roomname=roomname;
      socket.username=sender;

      nicknames = nicknames.filter(function(item)
      {
        return item!=null;
      });

      ids = ids.filter(function(item){
        return item!=null;
      });

      for (var i = count-1; i >= 0; i--)
      {
        if(nicknames[i]==rcv) {
          if(ids[i]!=null){
            io.to(ids[i]).emit('new message', {
              username: socket.username,
              message: data
            });
            id=1;
            break;
          }}
        }
        if(id==0) {

          input_chat_room_name=socket.roomname;
          input_sender = socket.username;
          input_receiver = rcv;
          input_msg = data;
          input_date = moment().format('YYMMDD');
          input_time = moment().format('HHmmss');
          insert_chat(input_chat_room_name, input_sender, input_receiver, input_msg, input_date, input_time);
        }

      });



      socket.on('chat add user', function (username,roomname) {

        if (addedUser == true) {
          return;
        }
        ids[count] = socket.id;

        socket.username = username;
        socket.roomname = roomname;
        nicknames[count] = username;
        numUsers++;
        count++;
        addedUser = true;

        find_message(username,roomname);

        setTimeout(function()
        {
          if(remaining_msg_true == 'yes')
          {
            for(i = 0; i < remaining_messages.length; i++){

              io.to(socket.id).emit('new message', {
                username: remaining_sender[i],
                message: remaining_messages[i]
              });

                }

                remaining_msg_true = 'no';
                remove_message(username,roomname);
              }

            }, 1500);

            socket.emit('login');
          });


          socket.on('disconnect', function () {
            console.log('in function disconnect : '+socket.username);

            for (var i = 0; i < ids.length; i++) {
              if(ids[i]==socket.id)
              {
                ids.splice(i,1);
              }
            }

            for (var i = 0; i < nicknames.length; i++) {
              if(socket.username==nicknames[i]){
                nicknames.splice(i, 1);
              }
            }

            nicknames = nicknames.filter(function(item){
              return item!=null;
            });
            ids = ids.filter(function(item){
              return item!=null;
            });

            if (addedUser == true) {
              numUsers--;
            }
          });
});


//DATA BASE


function connect_chat_db(){

}

function insert_chat(chat_room_name_sql, sender_sql, receiver_sql, msg_sql, date_sql, time_sql){


  var sql = 'INSERT INTO chat(chat_room_name, sender, receiver, msg, date, time) VALUES(?, ?, ?, ?, ?, ?)';
  var param = [chat_room_name_sql, sender_sql, receiver_sql, msg_sql, date_sql, time_sql];

  connection.query(sql, param, function(err, rows, fields){
    if (err){
      console.log(err);
    }
    else{
      console.log(rows.insertId);
    }
  });
}


function find_message(receiver_sql,chat_room_name_sql){

  var sql = 'select sender, msg from chat where receiver=? and chat_room_name = ?';

  var param = [receiver_sql,chat_room_name_sql];

  connection.query(sql, param, function(err, rows, fields){
    if (err){
      console.log(err);

    }
    else{

      if(rows.length!=0)
      {
        console.log('User info is  : ', rows);
        for(var i = 0; i < rows.length; i++)
        {
          remaining_messages[i] = rows[i].msg;
          remaining_sender[i] = rows[i].sender;
          remaining_msg_true = 'yes';
        }

      }
    }
  });
}

function remove_message(receiver_sql,chat_room_name_sql){
  var sql = 'delete from chat where receiver = ? and chat_room_name = ?';
  var param = [receiver_sql,chat_room_name_sql];

  connection.query(sql, param, function(err, rows, fields){
    if (err){
      console.log(err);
    }
    else{
      console.log("remove_message 제거했습니다.");
    }
});
}
