var mongoose = require("mongoose");
var Schema = mongoose.Schema;

const walkingSchema = new Schema({
    calories: {
        type: Number,
        default: 0
    },
    distance: {
        type: Number,
        default: 0,
    },
    date: {
        type: Date,
        default: Date.now,
    },
    walkingTime: {
        type: Number,
        default: 0
    },
    walkingAmounts: [{
        animal: {
            type: String,
        },
        amount: {
            type: Number,
            default: 0
        }
    }],
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
    }],
    toiletLoc: [{
        lat: {
            type: Number,
        },
        lon: {
            type: Number
        }
    }]
});

module.exports = mongoose.model("Walking", walkingSchema);
