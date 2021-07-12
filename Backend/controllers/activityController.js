const Activity = require('../models/Activity');
const moment = require('moment')


function withoutTime(dateTime) {
    var date = new Date(dateTime.getTime());
    date.setHours(0, 0, 0, 0);
    return date;
}

class ActivityController {
    addActivity(req, res) {
        const date = moment().format("YYYY/MM/DD")
        Activity.findOne({ user: req.user._id, date: date }, function (err, activity) {
            if (err) {
                return res.status(500).json({
                    success: false,
                    message: err.message
                });
            }

            if (!activity) {
                Activity.create({
                    user: req.user._id,
                    vehicle: req.body.vehicle,
                    date: date,
                    activity: req.body.activity
                    // activity : [ { start_time : req.body.start_time }]

                }, function (err, activity) {
                    if (err) {
                        return res.status(500).json({
                            success: false,
                            message: err.message
                        });
                    }

                    return res.status(200).json({
                        success: true,
                        message: "Activity Started Recording",

                    });
                });
            } else {
                // console.log(req.body.end_time)
                if (req.body.end_time !== undefined && req.body.end_time !== null && req.body.end_time !== "") {
                    var speed = 0, kilometer = 0;
                    activity.activity.map(a=>{
                        let s = a.speed
                        let d = a.kilometer
                        if(a.speed == undefined){
                            s = req.body.speed
                            d = req.body.kilometer
                        }
                        
                        speed += s
                        kilometer += d
                    })
                    let data = {
                        avg_speed : speed/activity.activity.length,
                        distance : kilometer/activity.activity.length,
                        "activity.$.end_time": req.body.end_time,
                        "activity.$.speed": req.body.speed,
                        "activity.$.kilometer": req.body.kilometer
                    };

                    

                    Activity.updateOne({_id:activity._id,"activity.start_time":req.body.start_time}, { $set: data }, function (err, activity) {
                        if (err) {
                            return res.status(500).json({
                                success: false,
                                message: err.message
                            });
                        }

                        return res.status(200).json({
                            success: true,
                            message: "Activity recording ended",

                        });
                    });

                    
                    
                } else {
                    
                    Activity.findByIdAndUpdate(activity._id, { $push: {"activity":req.body.activity} }, function (err, activity) {
                        if (err) {
                            return res.status(500).json({
                                success: false,
                                message: err.message
                            });
                        }
                        return res.status(200).json({
                            success: true,
                            message: "Activity Started Recording",

                        });

                    });
                }
            }
        });
    }

    getActivity(req, res) {

        Activity.aggregate([
            //   {
            //       $match : {user : req.body._id}
            //   }
            //   ,
            {
                $group: { _id: req.params._id, totalAvgSpeed: { $avg: '$avg_speed' }, totalDistance: { $sum: '$distance' } }
            }
        ],
            function (err, activity) {
                if (err) {
                    return res.status(500).json({
                        success: false,
                        message: err.message
                    });
                }
                console.log(activity[0]);

                return res.status(200).json({
                    success: true,
                    message: "Activity",
                    activity: activity[0] || null
                });
            });
    }

}

module.exports = ActivityController;