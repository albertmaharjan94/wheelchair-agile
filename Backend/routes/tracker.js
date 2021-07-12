const express = require('express');
const router = express.Router();

const trackerController = require('../controllers/trackerController');

const tokenHandler = require('../middleware/tokenHandler');

const tracker = new trackerController;

router.post('/', tokenHandler.checkUserToken, tracker.addTracker);
router.get('/', tokenHandler.checkUserToken, tracker.getTracker);

module.exports = router;






