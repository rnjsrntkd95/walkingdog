var mongoose = require("mongoose");
var Schema = mongoose.Schema;

const locationSchema = new Schema({
    region: {
        type: String,
    },
    city: {
        type: String,
    },
    street: {
        type: String,
    }
})


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
    location: {
        // type: locationSchema,
        type: String,
        required: true,
    },
    animal: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Animal',
        required: true,
    }],
    user: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true,
    },
});

module.exports = mongoose.model("Walking", walkingSchema);
