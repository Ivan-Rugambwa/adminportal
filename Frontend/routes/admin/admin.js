const express = require('express');
const router = express.Router();
const path = require('path');

router.get("/", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/homepage.html'))
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
router.get("/business/register", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/business/registerBusiness.html'))
})

router.get("/business", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/business/business.html'))
})
router.get("/business/edit", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/business/editBusiness.html'))
})
router.get("/seat", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/seat/seat.html'))
})

router.get("/seat/edit", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/seat/editSeat.html'))
})

router.get("/seat/register", (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/admin/seat/registerSeat.html'))
})



module.exports = router;