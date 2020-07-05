import http from 'k6/http';
import {check } from 'k6';

export let options = {
    stages: [
        { duration: "5s", target: 10 }, // simulate ramp-up of traffic
        { duration: "5s", target: 10 }, // stay at 10 users
        { duration: "5s", target: 0 }, // ramp-down to 0 users
    ],
    thresholds: {
        'http_req_duration': ['p(99)<500'], // 99% of requests must complete below 0.5s
    }
};

export default () => {
    var url = 'https://qp38z2339h.execute-api.eu-central-1.amazonaws.com/favorites';
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