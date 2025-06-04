import requests
from bs4 import BeautifulSoup
import time
import random
import json
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from datetime import datetime
import logging
import re
from urllib.parse import urljoin, urlparse

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

# Improved URL lists
NATIONAL_URLS = [
    "https://patch.com/",
    "https://patch.com/us/across-america"
]

LOCAL_URLS = [
    "https://patch.com/california/los-angeles",
    "https://patch.com/new-york/new-york-city",
    "https://patch.com/illinois/chicago",
    "https://patch.com/texas/houston",
    "https://patch.com/california/san-francisco",
    "https://patch.com/massachusetts/boston",
    "https://patch.com/washington/seattle",
    "https://patch.com/florida/miami",
    "https://patch.com/georgia/atlanta",
    "https://patch.com/arizona/phoenix",
    "https://patch.com/pennsylvania/philadelphia",
    "https://patch.com/michigan/detroit"
]

def setup_driver():
    """Enhanced WebDriver setup with better configuration"""
    options = Options()
    options.add_argument("--headless")
    options.add_argument("--no-sandbox")
    options.add_argument("--disable-dev-shm-usage")
    options.add_argument("--disable-blink-features=AutomationControlled")
    options.add_experimental_option("excludeSwitches", ["enable-automation"])
    options.add_experimental_option('useAutomationExtension', False)
    options.add_argument("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")

    try:
        driver = webdriver.Chrome(options=options)
        driver.execute_script("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})")
        driver.set_page_load_timeout(30)
        return driver
    except Exception as e:
        logger.error(f"Driver setup error: {e}")
        return None

def is_article_url(url):
    """Improved article URL detection"""
    if not url or 'patch.com' not in url:
        return False

    # More specific exclusions
    exclude_patterns = [
        r'/login', r'/signup', r'/about', r'/contact', r'/advertise',
        r'/search', r'/tag/', r'/category/', r'/author/',
        r'/events', r'/calendar', r'/classifieds', r'/directory'
    ]

    url_lower = url.lower()
    if any(re.search(pattern, url_lower) for pattern in exclude_patterns):
        return False

    # Look for article-like patterns
    # Patch articles typically have format: /state/city/article-title-with-hyphens
    url_parts = url.split('/')
    if len(url_parts) >= 4:
        # Check if last part looks like an article slug
        last_part = url_parts[-1]
        if len(last_part) > 10 and '-' in last_part:
            return True

    return False

def get_article_links(base_url, max_links=30):
    """Enhanced article link extraction"""
    driver = setup_driver()
    if not driver:
        return []

    links = set()

    try:
        logger.info(f"Fetching links from: {base_url}")
        driver.get(base_url)

        # Wait for page to load
        WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.TAG_NAME, "body"))
        )

        # Scroll to load more content
        for i in range(5):
            driver.execute_script(f"window.scrollTo(0, document.body.scrollHeight * {(i+1)*0.2});")
            time.sleep(2)

        # Multiple selectors to find article links
        selectors = [
            'a[href*="patch.com"]',
            '.headline a',
            '.story-card a',
            '.article-card a',
            'h1 a', 'h2 a', 'h3 a',
            '.post-title a',
            '[class*="title"] a',
            '[class*="headline"] a'
        ]

        for selector in selectors:
            try:
                elements = driver.find_elements(By.CSS_SELECTOR, selector)
                logger.info(f"Found {len(elements)} elements with selector: {selector}")

                for element in elements:
                    try:
                        href = element.get_attribute('href')
                        if href and is_article_url(href):
                            # Clean URL
                            clean_url = href.split('?')[0].split('#')[0]
                            links.add(clean_url)

                            if len(links) >= max_links:
                                break
                    except Exception as e:
                        continue

                if len(links) >= max_links:
                    break

            except Exception as e:
                logger.warning(f"Error with selector {selector}: {e}")
                continue

        logger.info(f"Found {len(links)} article links from {base_url}")

    except Exception as e:
        logger.error(f"Error getting links from {base_url}: {e}")
    finally:
        driver.quit()

    return list(links)

def extract_date(soup):
    """Extract publication date from various possible locations"""
    date_selectors = [
        'time[datetime]',
        '[class*="date"]',
        '[class*="published"]',
        '[class*="timestamp"]',
        '.byline time',
        '.article-meta time'
    ]

    for selector in date_selectors:
        element = soup.select_one(selector)
        if element:
            # Try datetime attribute first
            date_attr = element.get('datetime')
            if date_attr:
                return date_attr[:19]  # Keep YYYY-MM-DD HH:MM:SS format

            # Try text content
            text = element.get_text().strip()
            if text and len(text) > 5:
                return text[:50]  # Limit length

    return datetime.now().strftime("%Y-%m-%d")

def get_article_content(url):
    """Enhanced content extraction"""
    driver = setup_driver()
    if not driver:
        return None

    try:
        logger.info(f"Scraping content from: {url}")
        driver.get(url)

        # Wait for content to load
        WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.TAG_NAME, "body"))
        )
        time.sleep(3)

        html = driver.page_source
        soup = BeautifulSoup(html, "html.parser")

        # Remove unwanted elements
        for tag in soup(['script', 'style', 'nav', 'header', 'footer',
                         'aside', 'iframe', 'noscript', '.ad', '.advertisement']):
            tag.decompose()

        # Extract title
        title = "No Title"
        title_selectors = [
            'h1',
            '.headline',
            '.article-title',
            '.post-title',
            '[class*="title"]',
            'title'
        ]

        for selector in title_selectors:
            element = soup.select_one(selector)
            if element:
                text = element.get_text().strip()
                if len(text) > 5 and len(text) < 200:
                    title = text
                    break

        # Extract date
        date = extract_date(soup)

        # Extract content
        content_parts = []

        # Try to find main content area first
        main_content = soup.select_one('.article-content, .post-content, .entry-content, main, article')
        if main_content:
            paragraphs = main_content.find_all('p')
        else:
            paragraphs = soup.find_all('p')

        for p in paragraphs:
            text = p.get_text().strip()

            # Filter out unwanted content
            if (len(text) > 20 and
                    not any(skip in text.lower() for skip in [
                        'subscribe', 'sign up', 'follow us', 'advertisement',
                        'patch.com', 'cookie', 'privacy policy', 'terms of service',
                        'share this', 'related:', 'see also:', 'click here'
                    ]) and
                    not text.startswith('•') and
                    not re.match(r'^[\d\.\-\s]+$', text)):  # Skip number-only lines

                content_parts.append(text)

        full_text = '\n\n'.join(content_parts)

        # Validate content quality
        if len(full_text) < 100:
            logger.warning(f"Content too short for {url}: {len(full_text)} chars")
            return None

        word_count = len(full_text.split())
        if word_count < 20:
            logger.warning(f"Word count too low for {url}: {word_count} words")
            return None

        article_data = {
            "url": url,
            "title": title,
            "date": date,
            "text": full_text,
            "word_count": word_count,
            "scraped_at": datetime.now().isoformat()
        }

        logger.info(f"✅ Successfully scraped: {title[:50]}... ({word_count} words)")
        return article_data

    except Exception as e:
        logger.error(f"Error scraping {url}: {e}")
        return None
    finally:
        driver.quit()

def scrape_articles():
    """Main scraping function with improved error handling"""
    dataset = []
    processed_urls = set()

    logger.info("🚀 Starting enhanced scraper: 30 national + 120 local = 150 articles")

    # 1. NATIONAL ARTICLES (30)
    logger.info("\n=== NATIONAL ARTICLES ===")
    national_target = 30
    national_count = 0

    for base_url in NATIONAL_URLS:
        if national_count >= national_target:
            break

        links = get_article_links(base_url, max_links=50)
        random.shuffle(links)

        for link in links:
            if national_count >= national_target:
                break
            if link in processed_urls:
                continue

            processed_urls.add(link)
            article = get_article_content(link)

            if article:
                article['type'] = 'national'
                article['source_url'] = base_url
                dataset.append(article)
                national_count += 1
                logger.info(f"✅ National article {national_count}/30: {article['title'][:50]}...")

            # Random delay between requests
            time.sleep(random.uniform(3, 6))

    # 2. LOCAL ARTICLES (120)
    logger.info("\n=== LOCAL ARTICLES ===")
    local_target = 120
    local_count = 0
    articles_per_region = max(1, local_target // len(LOCAL_URLS))

    for i, base_url in enumerate(LOCAL_URLS):
        if local_count >= local_target:
            break

        region_count = 0
        links = get_article_links(base_url, max_links=25)
        random.shuffle(links)

        for link in links:
            if region_count >= articles_per_region or local_count >= local_target:
                break
            if link in processed_urls:
                continue

            processed_urls.add(link)
            article = get_article_content(link)

            if article:
                article['type'] = 'local'
                article['region'] = base_url.split('/')[-1] if '/' in base_url else f"region_{i}"
                article['source_url'] = base_url
                dataset.append(article)
                region_count += 1
                local_count += 1
                logger.info(f"✅ Local article {local_count}/120 (region {region_count}): {article['title'][:50]}...")

            # Random delay between requests
            time.sleep(random.uniform(3, 6))

    return dataset

def save_data(articles):
    """Save data with enhanced metadata"""
    timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')
    filename = f"patch_articles_{timestamp}.json"

    # Add metadata
    data = {
        "metadata": {
            "scraped_at": datetime.now().isoformat(),
            "total_articles": len(articles),
            "national_articles": len([a for a in articles if a.get('type') == 'national']),
            "local_articles": len([a for a in articles if a.get('type') == 'local']),
            "total_words": sum(a.get('word_count', 0) for a in articles),
            "scraper_version": "2.0"
        },
        "articles": articles
    }

    try:
        with open(filename, "w", encoding="utf-8") as f:
            json.dump(data, f, ensure_ascii=False, indent=2)

        logger.info(f"💾 Saved to {filename}")
        return filename
    except Exception as e:
        logger.error(f"Save error: {e}")
        return None

if __name__ == "__main__":
    start_time = datetime.now()

    try:
        articles = scrape_articles()

        if articles:
            filename = save_data(articles)

            # Statistics
            national = [a for a in articles if a.get('type') == 'national']
            local = [a for a in articles if a.get('type') == 'local']
            total_words = sum(a.get('word_count', 0) for a in articles)

            print(f"\n{'='*60}")
            print(f"✅ SUCCESS!")
            print(f"📊 Total articles: {len(articles)}")
            print(f"🇺🇸 National: {len(national)}")
            print(f"🏘️ Local: {len(local)}")
            print(f"📝 Total words: {total_words:,}")
            print(f"⏱️ Time elapsed: {datetime.now() - start_time}")
            print(f"💾 Saved to: {filename}")
            print(f"{'='*60}")

            # Sample titles
            if articles:
                print(f"\n📰 Sample articles:")
                for i, article in enumerate(articles[:5]):
                    print(f"  {i+1}. {article['title'][:60]}...")
        else:
            logger.error("❌ No articles were successfully scraped")
            print("\n🔍 Troubleshooting suggestions:")
            print("1. Check internet connection")
            print("2. Verify Chrome/ChromeDriver compatibility")
            print("3. Try running with --disable-headless for debugging")

    except KeyboardInterrupt:
        print("\n🛑 Scraping interrupted by user")
    except Exception as e:
        logger.error(f"💥 Critical error: {e}")
        import traceback
        traceback.print_exc()