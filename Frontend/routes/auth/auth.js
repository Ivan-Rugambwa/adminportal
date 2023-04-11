const express = require('express');
const router = express.Router();
const path = require('path');

router.get('/login', (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/auth/inlogg.html'))
})
router.get('/unauthorized', (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/auth/unauthorized.html'))
})
router.get('/error', (req, res) => {
    res.sendFile(path.join(__dirname) + '../../public/html/error.html');
});
module.exports = router;