const express = require('express');
const router = express.Router();
const path = require('path');

router.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, '../public/html/index.html'));
});
router.get('/error', (req, res) => {
    res.sendFile(path.join(__dirname, '../public/html/error.html'));
});
router.get('/404', (req, res) => {
    res.sendFile(path.join(__dirname, '../public/html/404.html'));
});
router.get('/error', (req, res) => {
    res.sendFile(path.join(__dirname, '../public/html/error.html'));
});

router.all('*', (req, res) => {
    res.status(404);
    res.sendFile(path.join(__dirname, '../public/html/404.html'));
});

module.exports = router;