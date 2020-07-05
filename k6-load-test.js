import http from 'k6/http';
import {check } from 'k6';

export let options = {
    stages: [
        { duration: "10s", target: 100 }, // simulate ramp-up of traffic
        { duration: "5s", target: 100 }, // stay at 100 users
        { duration: "10s", target: 0 }, // ramp-down to 0 users
    ],
    thresholds: {
        'http_req_duration': ['p(99)<500'],
    }
};

export default () => {
    var url = 'https://ln1yvwzv87.execute-api.eu-central-1.amazonaws.com/favorites';
    var payload = JSON.stringify({
        name: 'driss',
    });

    var params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    let response = http.post(url, payload, params);
    check(response, {
        'is status 200': r => r.status === 200,
        'is id present': r => r.json().hasOwnProperty('id'),
        'is name driss': r => r.json().name === 'driss'
    });
}