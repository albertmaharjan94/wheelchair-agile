const express = require('express');
const router = express.Router();
const authController = require('../controllers/authController');
const user = require('../models/User')
const tokenHandler = require('../middleware/tokenHandler');
const { isAdmin } = require('../middleware/roleAuth');
const { check } = require('express-validator');


let auth = new authController;



router.post(
    '/register',
    [
        check('fullname', "Name is required!!").not().isEmpty(),
        check('email', "Enter a valid email!!").isEmail().normalizeEmail()
            .custom(async (email) => {
                const existingUser = await user.findOne({ email });
                if (existingUser) {
                    throw new Error('Email already in use');
                }
            }),
        check('address', "Address is required!!").not().isEmpty(),
        check('contact', "Contact is required!!").not().isEmpty(),
        check('emContact', "Emergency Contact is required!!").not().isEmpty(),
        check('age', "Age is required!!").not().isEmpty(),
        check('password', "Password must be atleast of 8 character").not().isEmpty().isLength({ min: 8 })
    ],
    [tokenHandler.verifyAdmin, isAdmin],
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

router.get('/profile', tokenHandler.checkUserToken, auth.getProfile);

module.exports = router;

