const express = require('express');
const router = express.Router();
const admin = require('../models/Admin')
const tokenHandler = require('../middleware/tokenHandler');
const adminAuthController = require('../controllers/adminAuthController');
const { check } = require('express-validator');

let auth = new adminAuthController;

router.post(
    '/register',
    [
        check('email', "Enter a valid email!!").isEmail().normalizeEmail()
            .custom(async (email) => {
                const existingUser = await admin.findOne({ email });
                if (existingUser) {
                    throw new Error('Email already in use');
                }
            }),
        check('contact', "Contact is required!!").not().isEmpty(),
        check('role', "Please select role!!").not().isEmpty(),
        check('password', "Password must be atleast of 8 character").not().isEmpty().isLength({ min: 8 })
    ],
    auth.register
);

router.post(
    '/login',
    [
        check('email', "Email is not valid!!").isEmail().normalizeEmail(),
        check('password', "Enter the password").not().isEmpty()
    ],
    auth.login
    );

router.get('/profile', tokenHandler.verifyAdmin, auth.getProfile);

module.exports = router;

