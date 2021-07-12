const { models } = require('mongoose');
const admin = require('../models/Admin');
const jwt = require('jsonwebtoken');
let config = require('../config');
const bcrypt = require('bcrypt');
const { validationResult } = require('express-validator');



class AdminAuthController {
    login(req, res) {

        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(422).json({ errors: errors.array() });
        };

        admin.findOne({ email: req.body.email }, function (err, admin) {
            if (err) {
                return res.status(500).send('Error on the server');

            }

            if (!admin) {
                return res.status(401).json({
                    success: false,
                    message: 'Incorrent email or password',
                    token: null
                });
            }

            var passwordIsValid = bcrypt.compareSync(req.body.password, admin.password);
            if (!passwordIsValid) {
                return res.status(401).json({
                    success: false,
                    message: 'Incorrent email or password',
                    token: null
                });
            }


            let token = jwt.sign({ id: admin._id.toString() }, config.secret,
                {
                    expiresIn: '24h'
                });
            admin.tokens = admin.tokens.concat({ token: token });
            admin.save();

            return res.status(200).json({
                success: true,
                message: 'Authentication successful',
                token: token,
                role: admin.role,
                email: admin.email,
                adminid: admin._id
            });
        });
    }

    register(req, res) {

        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(422).json({ errors: errors.array()});
        };


        var hashedPassword = bcrypt.hashSync(req.body.password, 8);

        admin.create({
            email: req.body.email,
            contact: req.body.contact,
            role: req.body.role,
            password: hashedPassword

        },

            function (err, admin) {
                if (err) return res.status(500).send(err);

                let token = jwt.sign({ id: admin._id }, config.secret,
                    {
                        expiresIn: '24h'
                    });
                admin.tokens = admin.tokens.concat({ token: token });
                admin.save();

                return res.status(200).json({
                    success: true,
                    message: 'Registration successful',
                    token: token,
                    role: admin.role,
                    email: admin.email,
                    adminid: admin._id
                });
            }

        );
    }


    getProfile(req, res) {
        return res.status(200).json({
            success: true,
            message: "Admin",
            admin: {
                email: req.admin.email,
                contact: req.admin.contact,
                role: req.admin.role
            }
        });
    }

}

module.exports = AdminAuthController;