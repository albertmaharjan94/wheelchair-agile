const Tracker = require('../models/Tracker')

class TrackerController {
    addTracker(req, res){
        /**
         * --input--
         * body : {
                "vehicle": "60c9c2b2a91ffc0b2c7e2130",
                "location":
                {
                    "type" : "Point",
                    "coordinates" : [
                        27.8053082088538,
                        85.32936819601943
                    ]
                }
            }
        */
        Tracker.create({
            user : req.user._id,
            vehicle : req.body.vehicle,
            location: req.body.location
        }, function(err, tracker){
            if(err){
                return res.status(500).json({
                    success : false,
                    message : err.message
                })
            }

            return res.status(200).json({
                success : true,
                message : "Tracker recorded",
                tracker: tracker
            })
        });
    }

    getTracker(req, res){
        Tracker.find({
            user : req.user._id,
        }).
        sort({ createdAt: -1 }).exec(
        function(err, tracker){
            if(err){
                return res.status(500).json({
                    success : false,
                    messsage : err.message
                })
            }
            tracker = tracker.map((i,d ) => {
                return {
                    "vehicle" :  i.vehicle,
                    "lat" : i.location.coordinates[0],
                    "lon" : i.location.coordinates[1],
                    "timestamp" : i.createdAt
                }
            })
            return res.status(200).json({
                success: true,
                message : "Tracker",
                tracker : tracker
            })
        });
    }

}

module.exports = TrackerController