const verifyJWT = async (req, res, next) => {
    const jwt = req.header('Authorization');
    if (jwt === undefined) return res.redirect('/auth/unauthorized');

    const data = {token: jwt};
    const result = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response)
        .catch(error => {
            console.error('There was an error: ' + error);
        })
    if (!result.ok) {
        next();
    } else {
        return res.redirect('/auth/unauthorized');
    }
}

module.exports = verifyJWT;