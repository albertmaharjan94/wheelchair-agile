const { models } = require('mongoose');
const user = require('../models/User');
const tracker = require('../models/Tracker')
const jwt = require('jsonwebtoken');
let config = require('../config');
const bcrypt = require('bcrypt');
const { validationResult } = require('express-validator');


class AuthController {
    login(req, res) {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(200).json({success:false, errors: errors.array() });
        };

        user.findOne({ email: req.body.email }, function (err, user) {
            if (err) {
                return res.status(500).send('Error on the server');

            }

            if (!user) {
                return res.status(200).json({
                    success: false,
                    message: 'Incorrent username or password',
                    token: null
                });
            }

            var passwordIsValid = bcrypt.compareSync(req.body.password, user.password);
            if (!passwordIsValid) {
                return res.status(200).json({
                    success: false,
                    message: 'Incorrent username or password',
                    token: null
                });
            }


            let token = jwt.sign({ id: user._id.toString() }, config.secret,
                {
                    expiresIn: '24h'
                });
            user.tokens = user.tokens.concat({ token: token });
            user.save();

            return res.status(200).json({
                success: true,
                message: 'Authentication successful',
                data: {
                    token: token,
                    name: user.fullname,
                    email: user.email,
                    userid: user._id
                }
            });
        });
    }

    register(req, res) {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(422).json({ errors: errors.array() });
        };

        var hashedPassword = bcrypt.hashSync(req.body.password, 8);

        user.create({
            fullname: req.body.fullname,
            email: req.body.email,
            address: req.body.address,
            contact: req.body.contact,
            emContact: req.body.emContact,
            age: req.body.age,
            vehicle : req.body.vehicle,
            password: hashedPassword
        },

            function (err, user) {
                if (err) return res.status(500).send(err);

                let token = jwt.sign({ id: user._id }, config.secret,
                    {
                        expiresIn: '24h'
                    });
                user.tokens = user.tokens.concat({ token: token });
                user.save();

                return res.status(200).json({
                    success: true,
                    message: 'Authentication successful',
                    token: token,
                    name: user.fullname,
                    email: user.email,
                    userid: user._id
                });
            }

        );
    }


    async getProfile(req, res) {
        
        console.log(req.user._id)
        var track
        await tracker.find(
            {user:req.user._id},function(err,tracker){
                console.log(tracker)
                if(err){
                    return res.status(500).json({
                        success : false,
                        messsage : err.message
                    })
                }
                else{
                        track = tracker[0]  
                }
            })
        return res.status(200).json({
            success: true,
            message: "User",
            data: {
                userid : req.user._id,
                name: req.user.fullname,
                email: req.user.email,
                address: req.user.address,
                contact: req.user.contact,
                emContact: req.user.emContact,
                age: req.user.age,
                vehicle: track.vehicle,
                timestamp : track.createdAt
            }
        });
    }

}

module.exports = AuthController;