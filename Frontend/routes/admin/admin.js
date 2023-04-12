const express = require('express');
const router = express.Router();
const path = require('path');

router.get("/user/edit", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/editUser.html'))
})
router.get("/", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/adminportal.html'))
})

module.exports = router;