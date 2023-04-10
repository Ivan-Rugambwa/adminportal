const express = require('express');
const app = express();
const path = require('path');
const port = 3000;

app.use(express.static(path.join(__dirname, 'public'), {
    extensions: ['html', 'htm']
}));
app.use(express.json());

// app.use(cookieParser());

app.get("/", function (req, res) {
    res.send("Start page");
});
app.get("/seat", (req, res) => {
    res.sendFile(path.join(__dirname, '/public/html/user/seat.html'))
})
app.get("/login", (req, res) => {
    res.sendFile(path.join(__dirname, '/public/html/auth/inlogg.html'))
})
app.get("/seat/report", (req, res) => {
    res.sendFile(path.join(__dirname, '/public/html/user/fillSeat.html'))
})
app.get("/unauthorized", (req, res) => {
    res.sendFile(path.join(__dirname, '/public/html/auth/unauthorized.html'))
})

app.all('*', (req, res) => {
    res.status(404);
    if (req.accepts('html')) {
        res.sendFile(path.join(__dirname) + '/public/html/404.html');
    } else if (req.accepts('json')) {
        res.json({error: '404 Not Found'});
    } else {
        res.type('txt').send('404 Not Found');
    }
});

app.listen(port, function () {
    console.log(`Example app listening on port ${port}!`);
});

