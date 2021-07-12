



const {ADMIN} = require('../utility/roles')



const isAdmin = (req, res, next) =>{
    if(req.role === ADMIN){
        return next()
    }

    return res.status(401).json(
        {success : false,
        message : "Unauthorized"
    }
    )
}


module.exports = {
    isAdmin : isAdmin
}