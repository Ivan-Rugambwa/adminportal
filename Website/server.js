const express = require('express');
const app = express();
const path = require('path');
const port = 80;
const cookieParser = require("cookie-parser");

app.use(express.static(path.join(__dirname, 'public'), {
    extensions: ['html', 'htm']
}));
app.use(express.json());

app.use(cookieParser());

app.use('/auth', require('./routes/auth/auth'));
app.use('/seat', require('./routes/user/seat'));
app.use("/admin", require('./routes/admin/admin'));
app.use('/', require('./routes/root'));

app.listen(port, function () {
    console.log(`App listening on port ${port}!`);
});

