const Vehicle = require('../models/Vehicle');

class VehicleController{


    async addVehicle(req, res){   
        console.log(req.body)
        await Vehicle.create({
            vehicle_type : req.body.type,
            vehicle_number : req.body.number
        }, function(err, vehicle){
            if(err){
                return res.status(500).json({
                    success : false,
                    message : err.message
                })
            }

            return res.json({
                success : true,
                message : "Vehicle Added Successfully!",
                vehicle : vehicle
            })
        });
    }


    async getVehicle(req, res){
        await Vehicle.findById(req.params._id, function(err, vehicle){
            if(err){
                return res.status(500).json({
                    success : false,
                    messsage : err.message
                })
            }

            return res.status(200).json({
                success: true,
                message : "Vehicle",
                vehicle : vehicle
            })
        })
    }



}

module.exports = VehicleController