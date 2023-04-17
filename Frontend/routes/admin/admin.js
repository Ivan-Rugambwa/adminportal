const express = require('express');
const router = express.Router();
const path = require('path');

router.get("/", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/adminportal.html'))
})
router.get("/user", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/user/user.html'))
})
router.get("/user/edit", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/user/editUser.html'))
})
router.get("/user/register", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/user/registerUser.html'))
})
router.get("/business", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/business/business.html'))
})

module.exports = router;