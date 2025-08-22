fetch('/api/news')
  .then(response => response.json())
  .then(data => {
    const container = document.getElementById('rss-feed-list');
    container.innerHTML = ''; // Clear any existing content

    data.forEach(item => {
      const listItem = document.createElement('li');
      const link = document.createElement('a');
      link.href = item.link;
      link.target = '_blank'; // Open in a new tab
      link.textContent = item.title;

      listItem.appendChild(link);
      container.appendChild(listItem);
    });
  })
  .catch(error => {
    console.error('Error fetching news:', error);
    document.getElementById('rss-feed-list').innerHTML = '<li>Failed to load news.</li>';
  });