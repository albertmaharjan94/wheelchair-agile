const express = require('express');
const router = express.Router();

const activityController = require('../controllers/activityController');

const tokenHandler = require('../middleware/tokenHandler');
const activity = new activityController;

router.post('/', tokenHandler.checkUserToken,activity.addActivity);
router.get('/:_id', activity.getActivity);




module.exports = router;



