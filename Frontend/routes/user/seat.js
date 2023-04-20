const express = require('express');
const router = express.Router();
const path = require('path');

router.get('/report', (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/user/report.html'))
})
router.get("/", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/user/seat.html'))
})

module.exports = router;