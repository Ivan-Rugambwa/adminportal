const express = require('express');
const app = express();
const path = require('path');
const port = 3000;

app.use(express.static(path.join(__dirname, 'public'), {
    extensions: ['html', 'htm']
}));
app.use(express.json());

// app.use(cookieParser());

app.use('/auth', require('./routes/auth/auth'));
app.use('/seat', require('./routes/user/seat'));
app.get("/admin", (req, res) => {
    res.sendFile(path.join(__dirname, '/public/html/admin/adminportal.html'))
})
app.use('/', require('./routes/root'));

app.listen(port, function () {
    console.log(`App listening on port ${port}!`);
});

