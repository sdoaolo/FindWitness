var mysql = require('mysql');

var db_info = {
    host: 'localhost', // 'localhost'
    user: 'root',
    port: 5385,
    password: 'witness5385',
    database: 'CHAT_MESSAGE',
    socketPath: '/var/run/mysqld/mysqld.sock'
}

module.exports = {
    init: function () {
        return mysql.createConnection(db_info);
    },
    connect: function(conn) {
        conn.connect(function(err) {
            if(err) console.error('mysql connection error : ' + err);
            else console.log('mysql is connected successfully!');
        });
    }
}
