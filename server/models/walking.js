var mongoose = require("mongoose");
var Schema = mongoose.Schema;

const walkingSchema = new Schema({
    calorie: {
        type: Number,
        required: true,
        default: 0,
    },
    distance: {
        type: Number,
        required: true,
        default: 0,
    },
    date: {
        type: Date,
        default: Date.now,
    },
    routeImage: {
        type: String,
        // type: Buffer,
        // contentsType: String,
    },
    walkingTime: {
        type: Number,
        required: true,
    },
    walkingAmount: {
        type: Number,
    },
    addressAdmin: {
        type: String,
    },
    addressLocality: {
        type: String,
    },
    addressThoroughfare: {
        type: String,
    },
    animal: [{
        type: String,
        required: true,
    }],
    user: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true,
    },
    route: [{
        lat: {
            type: Number,
        },
        lon: {
            type: Number
        }
    }]
});

module.exports = mongoose.model("Walking", walkingSchema);
