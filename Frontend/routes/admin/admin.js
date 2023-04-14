const express = require('express');
const router = express.Router();
const path = require('path');

router.get("/user/edit", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/editUser.html'))
})
router.get("/", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/adminportal.html'))
})
router.get("/user", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/user.html'))
})
router.get("/business", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/business.html'))
})
router.get("/registration", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/registrationForm.html'))
})

module.exports = router;