let jwt = require('jsonwebtoken');
const User = require('../models/User'); 
const Admin = require('../models/Admin')
const config = require('../config.js');


module.exports.checkUserToken = (req, res, next) => {
    let token = req.headers['x-access-token'] || req.headers['authorization']; // Express headers are auto converted to lowercase
   

    if (token) {
      if (token.startsWith('Bearer ')) {
          // Remove Bearer from string
          token = token.slice(7, token.length);
        }
        
      jwt.verify(token, config.secret, async (err, decoded) => {
        if (err) {
          return res.json({
            success: false,
            message: 'Token is not valid'
          });
        } else {
          req.decoded = decoded;
          
          const user = await User.findOne({
            // _id: decoded.id, 'tokens.token': token
            _id: decoded.id
        })
        // console.log(decoded)
        if(!user){
          return res.json({
            success: false,
            message: 'Auth token is not supplied'
          });
        }
        req.user = user
        req.token = token
          next();
        }
      });
    } else {
      return res.json({
        success: false,
        message: 'Auth token is not supplied'
      });
    }
  };

module.exports.verifyAdmin = (req, res, next) => {
  let token = req.headers['x-access-token'] || req.headers['authorization']; // Express headers are auto converted to lowercase

  if (token) {
    if (token.startsWith('Bearer ')) {
        // Remove Bearer from string
        token = token.slice(7, token.length);
      }
      
    jwt.verify(token, config.secret, async (err, decoded) => {
      if (err) {
        return res.json({
          success: false,
          message: 'Token is not valid'
        });
      } else {
        req.decoded = decoded;
        
        const admin = await Admin.findOne({
          _id: decoded.id, 'tokens.token': token
      })
      
      if(!admin){
        return res.json({
          success: false,
          message: 'Auth token is not supplied'
        });
      }
      req.admin = admin
      req.role = admin.role;
      req.token = token
        next();
      }

    });
  } else {
    return res.json({
      success: false,
      message: 'Auth token is not supplied'
    });
  }
};

 
 
  